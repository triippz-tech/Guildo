import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITempBan, defaultValue } from 'app/shared/model/bot/temp-ban.model';

export const ACTION_TYPES = {
  FETCH_TEMPBAN_LIST: 'tempBan/FETCH_TEMPBAN_LIST',
  FETCH_TEMPBAN: 'tempBan/FETCH_TEMPBAN',
  CREATE_TEMPBAN: 'tempBan/CREATE_TEMPBAN',
  UPDATE_TEMPBAN: 'tempBan/UPDATE_TEMPBAN',
  DELETE_TEMPBAN: 'tempBan/DELETE_TEMPBAN',
  RESET: 'tempBan/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITempBan>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TempBanState = Readonly<typeof initialState>;

// Reducer

export default (state: TempBanState = initialState, action): TempBanState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEMPBAN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEMPBAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEMPBAN):
    case REQUEST(ACTION_TYPES.UPDATE_TEMPBAN):
    case REQUEST(ACTION_TYPES.DELETE_TEMPBAN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPBAN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEMPBAN):
    case FAILURE(ACTION_TYPES.CREATE_TEMPBAN):
    case FAILURE(ACTION_TYPES.UPDATE_TEMPBAN):
    case FAILURE(ACTION_TYPES.DELETE_TEMPBAN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPBAN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPBAN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEMPBAN):
    case SUCCESS(ACTION_TYPES.UPDATE_TEMPBAN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEMPBAN):
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

const apiUrl = 'services/bot/api/temp-bans';

// Actions

export const getEntities: ICrudGetAllAction<ITempBan> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEMPBAN_LIST,
  payload: axios.get<ITempBan>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITempBan> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPBAN,
    payload: axios.get<ITempBan>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITempBan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEMPBAN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITempBan> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEMPBAN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITempBan> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEMPBAN,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
