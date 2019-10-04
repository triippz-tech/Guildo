import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IUserStrike, defaultValue } from 'app/shared/model/bot/user-strike.model';

export const ACTION_TYPES = {
  FETCH_USERSTRIKE_LIST: 'userStrike/FETCH_USERSTRIKE_LIST',
  FETCH_USERSTRIKE: 'userStrike/FETCH_USERSTRIKE',
  CREATE_USERSTRIKE: 'userStrike/CREATE_USERSTRIKE',
  UPDATE_USERSTRIKE: 'userStrike/UPDATE_USERSTRIKE',
  DELETE_USERSTRIKE: 'userStrike/DELETE_USERSTRIKE',
  RESET: 'userStrike/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserStrike>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type UserStrikeState = Readonly<typeof initialState>;

// Reducer

export default (state: UserStrikeState = initialState, action): UserStrikeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERSTRIKE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERSTRIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_USERSTRIKE):
    case REQUEST(ACTION_TYPES.UPDATE_USERSTRIKE):
    case REQUEST(ACTION_TYPES.DELETE_USERSTRIKE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_USERSTRIKE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERSTRIKE):
    case FAILURE(ACTION_TYPES.CREATE_USERSTRIKE):
    case FAILURE(ACTION_TYPES.UPDATE_USERSTRIKE):
    case FAILURE(ACTION_TYPES.DELETE_USERSTRIKE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERSTRIKE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_USERSTRIKE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERSTRIKE):
    case SUCCESS(ACTION_TYPES.UPDATE_USERSTRIKE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERSTRIKE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/user-strikes';

// Actions

export const getEntities: ICrudGetAllAction<IUserStrike> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERSTRIKE_LIST,
    payload: axios.get<IUserStrike>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IUserStrike> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERSTRIKE,
    payload: axios.get<IUserStrike>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IUserStrike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERSTRIKE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IUserStrike> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERSTRIKE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserStrike> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERSTRIKE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
