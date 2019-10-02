import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IAutoModMentions, defaultValue } from 'app/shared/model/bot/auto-mod-mentions.model';

export const ACTION_TYPES = {
  FETCH_AUTOMODMENTIONS_LIST: 'autoModMentions/FETCH_AUTOMODMENTIONS_LIST',
  FETCH_AUTOMODMENTIONS: 'autoModMentions/FETCH_AUTOMODMENTIONS',
  CREATE_AUTOMODMENTIONS: 'autoModMentions/CREATE_AUTOMODMENTIONS',
  UPDATE_AUTOMODMENTIONS: 'autoModMentions/UPDATE_AUTOMODMENTIONS',
  DELETE_AUTOMODMENTIONS: 'autoModMentions/DELETE_AUTOMODMENTIONS',
  RESET: 'autoModMentions/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoModMentions>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModMentionsState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModMentionsState = initialState, action): AutoModMentionsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODMENTIONS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODMENTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMODMENTIONS):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMODMENTIONS):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMODMENTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODMENTIONS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODMENTIONS):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMODMENTIONS):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMODMENTIONS):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMODMENTIONS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODMENTIONS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODMENTIONS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMODMENTIONS):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMODMENTIONS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMODMENTIONS):
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

const apiUrl = 'services/bot/api/auto-mod-mentions';

// Actions

export const getEntities: ICrudGetAllAction<IAutoModMentions> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMODMENTIONS_LIST,
  payload: axios.get<IAutoModMentions>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoModMentions> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMODMENTIONS,
    payload: axios.get<IAutoModMentions>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoModMentions> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMODMENTIONS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoModMentions> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMODMENTIONS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoModMentions> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMODMENTIONS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
